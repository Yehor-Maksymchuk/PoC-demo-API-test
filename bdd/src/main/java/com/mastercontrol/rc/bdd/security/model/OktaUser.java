package com.mastercontrol.rc.bdd.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OktaUser {
    private String licenseType;
    private String username;
    private String password;
}
