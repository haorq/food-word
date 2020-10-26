package com.meiyuan.catering.core.util.bcrypt;
/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Implementation of PasswordEncoder that uses the Bcrypt strong hashing
 * function. Clients can optionally supply a "strength" (a.k.a. log rounds in
 * Bcrypt) and a SecureRandom instance. The larger the strength parameter the
 * more work will have to be done (exponentially) to hash the passwords. The
 * default value is 10.
 *
 * @author Dave Syer
 */
public class BcryptPasswordEncoder {
    private final Log logger = LogFactory.getLog(getClass());
    private final int strength;
    private final SecureRandom random;
    private Pattern bcryptPattern = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    public BcryptPasswordEncoder() {
        this(-1);
    }

    /**
     * @param strength
     *            the log rounds to use, between 4 and 31
     */
    public BcryptPasswordEncoder(int strength) {
        this(strength, null);
    }

    /**
     * @param strength
     *            the log rounds to use, between 4 and 31
     * @param random
     *            the secure random instance to use
     */
    public BcryptPasswordEncoder(int strength, SecureRandom random) {
        boolean b = strength != -1 && (strength < com.meiyuan.catering.core.util.bcrypt.Bcrypt.MIN_LOG_ROUNDS || strength > com.meiyuan.catering.core.util.bcrypt.Bcrypt.MAX_LOG_ROUNDS);
        if (b) {
            throw new IllegalArgumentException("Bad strength");
        }
        this.strength = strength;
        this.random = random;
    }

    public String encode(CharSequence rawPassword) {
        String salt;
        if (strength > 0) {
            if (random != null) {
                salt = com.meiyuan.catering.core.util.bcrypt.Bcrypt.gensalt(strength, random);
            } else {
                salt = com.meiyuan.catering.core.util.bcrypt.Bcrypt.gensalt(strength);
            }
        } else {
            salt = com.meiyuan.catering.core.util.bcrypt.Bcrypt.gensalt();
        }
        return com.meiyuan.catering.core.util.bcrypt.Bcrypt.hashpw(rawPassword.toString(), salt);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            logger.warn("Empty encoded password");
            return false;
        }

        if (!bcryptPattern.matcher(encodedPassword).matches()) {
            logger.warn("Encoded password does not look like Bcrypt");
            return false;
        }

        return com.meiyuan.catering.core.util.bcrypt.Bcrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
