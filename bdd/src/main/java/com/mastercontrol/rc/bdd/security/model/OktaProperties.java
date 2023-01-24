package com.mastercontrol.rc.bdd.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OktaProperties {
    private OktaCommon oktaCommon;
    private List<OktaUser> oktaUsers;
}
