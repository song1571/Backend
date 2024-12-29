package com.mingi.backend.UUID;

import java.util.UUID;

public class VerificationCodeGenerator {

    //128비트 랜덤값에서 -와 공백을 제외하고 처음 12자리만 가져옴
    public static String generatedVerificationCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12); 
    }
}
