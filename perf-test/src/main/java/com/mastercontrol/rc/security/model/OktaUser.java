package com.mastercontrol.rc.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OktaUser {
    private String licenseType;
    private String username;
    private String password;
}
