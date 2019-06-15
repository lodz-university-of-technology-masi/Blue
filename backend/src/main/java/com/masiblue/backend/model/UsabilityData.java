package com.masiblue.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "USABILITY_DATA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsabilityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="IP")
    private String ip;

    @Column(name="BROWSER")
    @Enumerated(value = EnumType.STRING)
    private Browser browser;

    @Column(name="USERNAME")
    private String username;

    @Column(name="M_ID")
    private int userMeasurementNumber;

    @Column(name="SAVETIME")
    private long saveTime;

    @Column(name="RES_W")
    private int screenWidth;

    @Column(name="RES_H")
    private int screenHeight;

    @Column(name="MC")
    private int mouseClicks;

    @Column(name="TIME")
    private int time;

    @Column(name="DIST")
    private long distance;

    @Column(name="FAIL")
    private int isFailed = 0;

    @Column(name="ERROR")
    private int errorCode;
}
