package org.example.dto;

import org.example.entity.Form;

import java.util.List;

public class FormRequest
{
    private Form form;
    private List<String> recipients;
    public Form getForm()
    {
        return form;
    }

    public List<String> getRecipients() {
        return recipients;
    }
}
