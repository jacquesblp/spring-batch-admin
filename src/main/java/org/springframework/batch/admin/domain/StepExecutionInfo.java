/*
 * Copyright 2009-2015 the original author or authors.
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
/**
 * 
 */
package org.springframework.batch.admin.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

public class StepExecutionInfo {

	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

	private SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");

	private Long id;

	private Long jobExecutionId;

	private String jobName;

	private String name;

	private String startDate = "-";

	private String startTime = "-";

	private String duration = "-";

	private StepExecution stepExecution;

	private long durationMillis;

	private String stepType = StepType.UNKNOWN.getDisplayName();

	private TimeZone timeZone = TimeZone.getTimeZone("UTC");

	public StepExecutionInfo(String jobName, Long jobExecutionId, String name, TimeZone timeZone) {
		this.jobName = jobName;
		this.jobExecutionId = jobExecutionId;
		this.name = name;
		this.stepExecution = new StepExecution(name, new JobExecution(jobExecutionId));

		if(timeZone != null) {
			this.timeZone = timeZone;
		}
	}

	public StepExecutionInfo(StepExecution stepExecution, TimeZone timeZone) {

		if(timeZone != null) {
			this.timeZone = timeZone;
		}

		this.stepExecution = stepExecution;
		this.id = stepExecution.getId();
		this.name = stepExecution.getStepName();
		this.jobName = stepExecution.getJobExecution() == null
				|| stepExecution.getJobExecution().getJobInstance() == null ? "?" : stepExecution.getJobExecution()
				.getJobInstance().getJobName();
		this.jobExecutionId = stepExecution.getJobExecutionId();
		// Duration is always in GMT
		durationFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (stepExecution.getStartTime() != null) {
			this.startDate = dateFormat.format(stepExecution.getStartTime());
			this.startTime = timeFormat.format(stepExecution.getStartTime());
			Date endTime = stepExecution.getEndTime() != null ? ldtToDate(stepExecution.getEndTime()) : new Date();
			this.durationMillis = endTime.getTime() - ldtToDate(stepExecution.getStartTime()).getTime();
			this.duration = durationFormat.format(new Date(durationMillis));
		}

	}


	private Date ldtToDate(LocalDateTime ldt) {
		if (ldt == null) {
			return null;
		}
		return Date.from(ldt.toInstant(ZoneOffset.UTC));
	}

	public Long getId() {
		return id;
	}

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	public String getName() {
		return name;
	}

	public String getJobName() {
		return jobName;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getDuration() {
		return duration;
	}

	public long getDurationMillis() {
		return durationMillis;
	}
	
	public String getStatus() {
		if (id!=null) {
			return stepExecution.getStatus().toString();
		}
		return "NONE";
	}

	public String getExitCode() {
		if (id!=null) {
			return stepExecution.getExitStatus().getExitCode();
		}
		return "NONE";
	}

	public StepExecution getStepExecution() {
		return stepExecution;
	}

	public String getStepType() { return this.stepType; }

	public TimeZone getTimeZone() {
		return timeZone;
	}
}