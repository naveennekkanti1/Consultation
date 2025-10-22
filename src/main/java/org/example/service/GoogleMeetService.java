package org.example.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.example.entity.MeetingModel;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleMeetService {

    private Calendar getCalendarService() throws Exception {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream("src/main/resources/credentials.json"))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Meeting Scheduler").build();
    }

    public String createGoogleMeet(MeetingModel meeting) throws Exception {
        Calendar service = getCalendarService();

        // Parse date and time
        String dateTimeString = meeting.getMeetingDate() + "T" + meeting.getMeetingTime();
        LocalDateTime startDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        Date startDate = Date.from(startDateTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant());

        // Create event
        Event event = new Event()
                .setSummary(meeting.getMeetingName())
                .setDescription(meeting.getMeetingDescription());

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startDate))
                .setTimeZone("Asia/Kolkata");
        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endDate))
                .setTimeZone("Asia/Kolkata");

        event.setStart(start);
        event.setEnd(end);

        // Enable Google Meet (no attendees for service account)
        ConferenceData conferenceData = new ConferenceData();
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        createConferenceRequest.setRequestId("meet-" + System.currentTimeMillis());
        conferenceData.setCreateRequest(createConferenceRequest);
        event.setConferenceData(conferenceData);

        // Insert event
        event = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return event.getHangoutLink();
    }
}
