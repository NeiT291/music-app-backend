package org.neit.musicapp.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TokenInfo {
    public String getUsername(){
        var context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }
}
