package com.example.system.data;

import java.io.Serializable;

public record UserSession(String sessionId, String sessionToken) implements Serializable {
}
