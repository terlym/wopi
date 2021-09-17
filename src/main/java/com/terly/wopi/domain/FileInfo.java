package com.terly.wopi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author terly
 * @date 2021-09-16 15:33
 */
@Data
public class FileInfo {
    @JsonProperty("BaseFileName")
    private String baseFileName;

    @JsonProperty("Size")
    private long size;

   /* @JsonProperty("OwnerId")
    private String ownerId;*/

    @JsonProperty("UserId")
    private String userId;

    @JsonProperty("UserCanWrite")
    private boolean userCanWrite;

  /*  private long version;

    private String sha256;

    private boolean allowExternalMarketplace;



    private boolean supportsUpdate;

    private boolean supportsLocks;*/
}
