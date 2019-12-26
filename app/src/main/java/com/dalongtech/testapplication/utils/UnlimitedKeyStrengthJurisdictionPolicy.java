package com.dalongtech.testapplication.utils;

import javax.crypto.Cipher;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;

// https://stackoverflow.com/questions/1179672/how-to-avoid-installing-unlimited-strength-jce-policy-files-when-deploying-an
/**
 * -------------------------------------
 * 作者：vitta@<WVitta@126.com>
 * -------------------------------------
 * 时间：2018/1/15 11:18
 * -------------------------------------
 * 描述：AES 128 位 转成 256 位，没有限制的方法
 * -------------------------------------
 * 备注：
 * -------------------------------------
 */
public class UnlimitedKeyStrengthJurisdictionPolicy {


    private static boolean isRestrictedCryptography() throws NoSuchAlgorithmException {
        return Cipher.getMaxAllowedKeyLength("AES/ECB/NoPadding") <= 128;
    }

    private static void removeCryptographyRestrictions() {
        try {
            if (!isRestrictedCryptography()) {
                System.out.println("Cryptography restrictions removal not needed");
                return;
            }
            /*
             * Do the following, but with reflection to bypass access checks:
             *
             * JceSecurity.isRestricted = false;
             * JceSecurity.defaultPolicy.perms.clear();
             * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
             */
            Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
            Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

            Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
            isRestrictedField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(isRestrictedField, isRestrictedField.getModifiers() & ~Modifier.FINAL);
            isRestrictedField.set(null, false);

            Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicyField.setAccessible(true);
            PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

            Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defaultPolicy)).clear();

            Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defaultPolicy.add((Permission) instance.get(null));

            System.out.println("Successfully removed cryptography restrictions");
        } catch (Exception e) {
            System.out.println("Failed to remove cryptography restrictions" + e);
        }
    }

    static {
        removeCryptographyRestrictions();
    }

    public static void ensure() {
        // just force loading of this class
    }
}