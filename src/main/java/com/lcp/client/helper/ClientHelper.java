package com.lcp.client.helper;

import com.lcp.client.entity.Client;
import com.lcp.util.RandomUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientHelper {
    private static final String CLIENT_CODE_FORMAT = "%s%06d";
    private static final String CLIENT_CODE_PREFIX = "CL";

    public static String genPassword() {
        return RandomUtil.generateCode(8);
    }

    public static String genClientCode(Client lastestClient) {
        long count = countClientInGroup(lastestClient);
        return String.format(CLIENT_CODE_FORMAT, CLIENT_CODE_PREFIX, ++count);
    }

    private static long countClientInGroup(Client latestClient) {
        int incrementNumberLength = 6;
        int maxLength = 8;
        if (latestClient == null
                || latestClient.getCode() == null
                || latestClient.getCode().length() < maxLength) {
            return 0;
        }
        String clientNumber = latestClient.getCode();
        String countClient = clientNumber.substring(clientNumber.length() - incrementNumberLength);
        return Long.parseLong(countClient);
    }
}
