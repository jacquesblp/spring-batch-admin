/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.admin.web.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.springframework.batch.admin.domain.JobExecutionInfo;
import org.springframework.batch.admin.domain.JobExecutionInfoResource;
import org.springframework.batch.admin.domain.JobInstanceInfoResource;
import org.springframework.batch.admin.web.BatchJobInstancesController;
import org.springframework.batch.admin.web.JobInstanceInfo;
import org.springframework.batch.core.JobExecution;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/**
 * Resource assembler that builds the REST resource
 * {@link JobInstanceInfoResource} out of domain model
 * {@link org.springframework.batch.admin.web.JobInstanceInfo}.
 * 
 * @author Ilayaperumal Gopinathan
 */
public class JobInstanceInfoResourceAssembler extends
        RepresentationModelAssemblerSupport<JobInstanceInfo, JobInstanceInfoResource> {

    JobExecutionInfoResourceAssembler jobExecutionInfoResourceAssembler = new JobExecutionInfoResourceAssembler();

    public JobInstanceInfoResourceAssembler() {
        super(BatchJobInstancesController.class, JobInstanceInfoResource.class);
    }

    @Override
    public JobInstanceInfoResource toModel(JobInstanceInfo entity) {
        return createModelWithId(entity.getJobInstance().getId(), entity);
    }

    @Override
    protected JobInstanceInfoResource instantiateModel(JobInstanceInfo entity) {
        Collection<JobExecution> jobExecutions = entity.getJobExecutions();
        List<JobExecutionInfoResource> infos = new ArrayList<JobExecutionInfoResource>(jobExecutions.size());

        for (JobExecution jobExecution : jobExecutions) {
            infos.add(jobExecutionInfoResourceAssembler
                    .toModel(new JobExecutionInfo(jobExecution, TimeZone.getTimeZone("UTC"))));
        }

        return new JobInstanceInfoResource(entity.getJobInstance(), infos);
    }
}
