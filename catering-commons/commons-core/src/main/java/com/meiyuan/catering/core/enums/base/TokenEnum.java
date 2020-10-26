package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * 描述: token
 *
 * @author zengzhangni
 * @date 2020/6/30 17:29
 * @since v1.2.0
 */
@Getter
public enum TokenEnum {
    /**
     * token
     */
    ADMIN("X-Dts-Admin-Token", 86400,
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKUTN81w5gWFGa9caFJrF9E4H7iJhPb6Wr1oJ0R6NujWt+VeDA69H/FHxazfhvAnmMcO0hAhs8WF6EZGL3LMQ5ixdHtlvctO0JrTsh1qZvNeu1nPBgiY8c+H4JNlzyH2a7WG8zaUjI6yPIl1aUmoJFUl4k9InotIVLGdMNf/7ccZAgMBAAECgYBG1NU38P6yVadsyJD29H72xkdOLqwAMfKnnT5kMrDG/gNQhydTV7GSHYLjp9JF0CfNGj3msB9sXQYKyWjosF88QEf/Xl2OGXMlS776KNg4h0K07rW/6KyHPfusEY5tVqu1fxzVkV8JP308NYzLQiIbTODjdkjNWRqv5vXariihFQJBANK7+xl/ir07frGpg8P5q1OmtNn8KVf841ONUIgpkXZolhmxaRDXl44uQLBNj1T1hYY1LcgNRhcAdgxL4cth/Z8CQQDIiH4OGBqwW/XYM5snX+6pG0BzV/mBIe5kyLbcWXpsUiPHpuZ3ISMIgRBrz9e4icBPzzYaFjZYgEssMYjhiZBHAkEAisziANNu8GDle+NpNIWYAQsGh6V9HcUlffzQFD883j7Yzqd5ymwWGMICPZ14UL8+aT1RoDiN98yH9GVunWU3hQJBALsVA6z4deW0VV43fvcz+37OislQ+NetJS/nRRm7bIgj9Xds0LOxOXLUndJHHDTwZnbOlOWvdVIJlpgLOm1XHj8CQFy24hhr241UHrbJSGqoa9Sxnsgu2H7etarw9RVh0LzaiOgR5rP0Ig/ZiXqSHwKsYyqRgV629Fz631+gRaNcyfE=",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClEzfNcOYFhRmvXGhSaxfROB+4iYT2+lq9aCdEejbo1rflXgwOvR/xR8Ws34bwJ5jHDtIQIbPFhehGRi9yzEOYsXR7Zb3LTtCa07IdambzXrtZzwYImPHPh+CTZc8h9mu1hvM2lIyOsjyJdWlJqCRVJeJPSJ6LSFSxnTDX/+3HGQIDAQAB"
    ),
    /**
     * 商户app
     */
    MERCHANT_APP("X-Dts-Admin-Token", 259200,
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKUTN81w5gWFGa9caFJrF9E4H7iJhPb6Wr1oJ0R6NujWt+VeDA69H/FHxazfhvAnmMcO0hAhs8WF6EZGL3LMQ5ixdHtlvctO0JrTsh1qZvNeu1nPBgiY8c+H4JNlzyH2a7WG8zaUjI6yPIl1aUmoJFUl4k9InotIVLGdMNf/7ccZAgMBAAECgYBG1NU38P6yVadsyJD29H72xkdOLqwAMfKnnT5kMrDG/gNQhydTV7GSHYLjp9JF0CfNGj3msB9sXQYKyWjosF88QEf/Xl2OGXMlS776KNg4h0K07rW/6KyHPfusEY5tVqu1fxzVkV8JP308NYzLQiIbTODjdkjNWRqv5vXariihFQJBANK7+xl/ir07frGpg8P5q1OmtNn8KVf841ONUIgpkXZolhmxaRDXl44uQLBNj1T1hYY1LcgNRhcAdgxL4cth/Z8CQQDIiH4OGBqwW/XYM5snX+6pG0BzV/mBIe5kyLbcWXpsUiPHpuZ3ISMIgRBrz9e4icBPzzYaFjZYgEssMYjhiZBHAkEAisziANNu8GDle+NpNIWYAQsGh6V9HcUlffzQFD883j7Yzqd5ymwWGMICPZ14UL8+aT1RoDiN98yH9GVunWU3hQJBALsVA6z4deW0VV43fvcz+37OislQ+NetJS/nRRm7bIgj9Xds0LOxOXLUndJHHDTwZnbOlOWvdVIJlpgLOm1XHj8CQFy24hhr241UHrbJSGqoa9Sxnsgu2H7etarw9RVh0LzaiOgR5rP0Ig/ZiXqSHwKsYyqRgV629Fz631+gRaNcyfE=",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClEzfNcOYFhRmvXGhSaxfROB+4iYT2+lq9aCdEejbo1rflXgwOvR/xR8Ws34bwJ5jHDtIQIbPFhehGRi9yzEOYsXR7Zb3LTtCa07IdambzXrtZzwYImPHPh+CTZc8h9mu1hvM2lIyOsjyJdWlJqCRVJeJPSJ6LSFSxnTDX/+3HGQIDAQAB"
    ),
    /**
     * 商户pc端登录过期时间3天
     */
    MERCHANT("X-Dts-Merchant-Token", 259200,
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOtCNOJUdLeXJDfZNf9WJRebzLwLqV06b1zMTaTaP6qHZcR/zWkSITRmkbO0AJMHboqF9LH6KvaNvrfidDoLfSNryB4VGrJB2t+1mNV8dU+TDokC3i9fcBDgC1RbJ0tslxfxjsFA+feQmjnKgZw57SgAOmaG/Ycy2PEq5r2Z3M/rAgMBAAECgYBrS9zWxPIesX5BQn3QILf2Foa/1Qm/jWExD7Tx1OBopCwT/L3wh+ZBAe696QDCDgeqggHiElj57hfnwg6EUq0VVK72V5BTEdcEy/MtzD0S35Pmwu2PfWkokaQlMX1r4HKYzIdiX9ZLwtM99a6yGV7mbkD+urAeDR+lGGxvoKxikQJBAPsTep5ZE7bPGNzUjQi+925G39HsMrEea56LyeXPwtF0FrDX9WuWug/vYq2nXZ4WqzlTYo5HZte+e2qGRT9c9y0CQQDv31D/gyjp5CI5ZPFI++Flhc/ytgwj+cnjkjjSicM9xuxcwOC0M75gC1LqqFTGEhC/pPkUdDSx723JIk6MT9J3AkByKWY8r9YV7k6owoCqNGK/IrzMb7CQ9nsKqRcRRomXwUg7+sYduHg7nKZqvUoFAtuyAicy+GZbvMHTKuA7smK1AkEAleGeBOn5iJvp3p76PTZV0k/YbbnewCP2SGI3ayZ1foZrblF+llq2m+C3gLSJs/HZ0UXN9T/fJUeL+9+Z4QaHZQJBALTUR37T+QYiKbQwKwzXZsDMoQVpdTb7+YVbWnBonWbSoxv/10vz1ZpIjvge5GgQhy11HIMShIx2anWfq/bMkwk=",
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrQjTiVHS3lyQ32TX/ViUXm8y8C6ldOm9czE2k2j+qh2XEf81pEiE0ZpGztACTB26KhfSx+ir2jb634nQ6C30ja8geFRqyQdrftZjVfHVPkw6JAt4vX3AQ4AtUWydLbJcX8Y7BQPn3kJo5yoGcOe0oADpmhv2HMtjxKua9mdzP6wIDAQAB"
    );
    private String token;
    private Integer expirationTime;
    private String privateKey;
    private String publicKey;

    TokenEnum(String token, Integer expirationTime, String privateKey, String publicKey) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }}
