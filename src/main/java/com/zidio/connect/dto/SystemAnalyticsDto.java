package com.zidio.connect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemAnalyticsDto {
    private long totalUsers;
    private long totalStudents;
    private long totalRecruiters;
    private long totalJobs;
    private long activeJobs;
    private long totalApplications;
}