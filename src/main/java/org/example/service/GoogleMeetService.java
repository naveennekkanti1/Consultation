package org.example.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.example.entity.MeetingModel;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleMeetService {

    private static final String CALENDAR_ID = "primary";
    private static final String APPLICATION_NAME = "SolAI Consultation";
    private static final String CREDENTIALS_PATH = "src/main/resources/credentials.json";

    public String createGoogleMeet(MeetingModel meetingModel) throws Exception {

        // ✅ FIX: Parse "06-06-2026" and "3:00" into proper DateTime
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        LocalDate date = LocalDate.parse(meetingModel.getMeetingDate(), dateFormatter);
        LocalTime time = LocalTime.parse(meetingModel.getMeetingTime(), timeFormatter);

        // Combine into ZonedDateTime using IST
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        ZonedDateTime startZdt = ZonedDateTime.of(date, time, zoneId);
        ZonedDateTime endZdt = startZdt.plusHours(1); // 1 hour meeting by default

        // Convert to Google's DateTime format (RFC 3339)
        com.google.api.client.util.DateTime startDateTime =
                new com.google.api.client.util.DateTime(startZdt.toInstant().toEpochMilli());
        com.google.api.client.util.DateTime endDateTime =
                new com.google.api.client.util.DateTime(endZdt.toInstant().toEpochMilli());

        // ✅ Load credentials from service account JSON
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(CREDENTIALS_PATH))
                .createScoped(Collections.singleton(
                        "https://www.googleapis.com/auth/calendar"
                ));

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APPLICATION_NAME).build();

        // ✅ Build the event
        Event event = new Event()
                .setSummary(meetingModel.getMeetingName())
                .setDescription(meetingModel.getMeetingDescription());

        event.setStart(new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Kolkata"));

        event.setEnd(new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Kolkata"));

        // ✅ Add attendee
        EventAttendee attendee = new EventAttendee()
                .setEmail(meetingModel.getContactEmail());
        event.setAttendees(List.of(attendee));

        // ✅ Add Google Meet conference
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey()
                .setType("hangoutsMeet");

        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest()
                .setRequestId("solai-" + System.currentTimeMillis())
                .setConferenceSolutionKey(conferenceSolutionKey);

        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(createConferenceRequest);

        event.setConferenceData(conferenceData);

        // ✅ Insert with conferenceDataVersion=1 to generate Meet link
        Event createdEvent = service.events()
                .insert(CALENDAR_ID, event)
                .setConferenceDataVersion(1)
                .setSendUpdates("all") // sends invite email to attendee
                .execute();

        // ✅ Extract the Meet link
        String meetLink = createdEvent
                .getConferenceData()
                .getEntryPoints()
                .stream()
                .filter(ep -> "video".equals(ep.getEntryPointType()))
                .findFirst()
                .map(EntryPoint::getUri)
                .orElse("https://meet.google.com");

        return meetLink;
    }
}