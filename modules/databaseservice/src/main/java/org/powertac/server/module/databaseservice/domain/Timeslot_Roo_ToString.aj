// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.powertac.server.module.databaseservice.domain;

import java.lang.String;

privileged aspect Timeslot_Roo_ToString {
    
    public String Timeslot.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Competition: ").append(getCompetition()).append(", ");
        sb.append("MeterReadings: ").append(getMeterReadings() == null ? "null" : getMeterReadings().size()).append(", ");
        sb.append("Orderbooks: ").append(getOrderbooks() == null ? "null" : getOrderbooks().size()).append(", ");
        sb.append("StartDateTime: ").append(getStartDateTime()).append(", ");
        sb.append("EndDateTime: ").append(getEndDateTime());
        return sb.toString();
    }
    
}
