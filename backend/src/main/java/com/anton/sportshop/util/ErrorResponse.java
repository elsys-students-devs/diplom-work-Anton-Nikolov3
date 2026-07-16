package com.anton.sportshop.util;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timeStamp, int status, String error, String message) {
}
