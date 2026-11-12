package com.fitpro.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordUtilTest {

    @Test
    @DisplayName("Password hashing produces salted string with correct format")
    public void testHashFormat() {
        String password = "StrongPassword123!";
        String hash = PasswordUtil.hash(password);

        Assertions.assertNotNull(hash);
        Assertions.assertTrue(hash.contains(":"), "Hash must contain salt and hash separator ':'");
        String[] parts = hash.split(":");
        Assertions.assertEquals(2, parts.length, "Hash must have exactly salt and hash parts");
        Assertions.assertFalse(parts[0].isEmpty(), "Salt must not be empty");
        Assertions.assertFalse(parts[1].isEmpty(), "Hash must not be empty");
    }

    @Test
    @DisplayName("Verification succeeds with matching password")
    public void testVerifySuccess() {
        String password = "SecretMemberPassword#2026";
        String hash = PasswordUtil.hash(password);

        Assertions.assertTrue(PasswordUtil.verify(password, hash), "Password verification must succeed for identical input");
    }

    @Test
    @DisplayName("Verification fails with incorrect password")
    public void testVerifyFailure() {
        String password = "SecretMemberPassword#2026";
        String hash = PasswordUtil.hash(password);

        Assertions.assertFalse(PasswordUtil.verify("WrongPassword", hash), "Password verification must reject wrong passwords");
        Assertions.assertFalse(PasswordUtil.verify("", hash), "Password verification must reject empty passwords");
        Assertions.assertFalse(PasswordUtil.verify(password.toLowerCase(), hash), "Password verification must be case-sensitive");
    }

    @Test
    @DisplayName("Unique cryptographic salts are used for each hash execution")
    public void testSaltUniqueness() {
        String password = "SamePasswordAcrossUsers";
        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);

        Assertions.assertNotEquals(hash1, hash2, "Two hashes of the same password must differ due to unique random salt");
        Assertions.assertTrue(PasswordUtil.verify(password, hash1));
        Assertions.assertTrue(PasswordUtil.verify(password, hash2));
    }

    @Test
    @DisplayName("Invalid or null hash verification returns false safely")
    public void testVerifyNullOrMalformed() {
        Assertions.assertFalse(PasswordUtil.verify(null, "someSalt:someHash"));
        Assertions.assertFalse(PasswordUtil.verify("mypassword", null));
        Assertions.assertFalse(PasswordUtil.verify("mypassword", "malformedHashWithoutColon"));
    }
}
