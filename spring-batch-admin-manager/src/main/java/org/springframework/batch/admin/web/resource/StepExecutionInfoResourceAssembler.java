/*
 * Copyright 2013-2015 the original author or authors.
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

import org.springframework.batch.admin.domain.StepExecutionInfo;
import org.springframework.batch.admin.domain.StepExecutionInfoResource;
import org.springframework.batch.admin.web.BatchStepExecutionsController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/**
 * Knows how to build a REST resource out of our domain model
 * {@link org.springframework.batch.admin.domain.StepExecutionInfo}.
 * 
 * @author Gunnar Hillert
 * @since 2.0
 */
public class StepExecutionInfoResourceAssembler extends
        RepresentationModelAssemblerSupport<StepExecutionInfo, StepExecutionInfoResource> {

    public StepExecutionInfoResourceAssembler() {
        super(BatchStepExecutionsController.class, StepExecutionInfoResource.class);
    }

    @Override
    public StepExecutionInfoResource toModel(StepExecutionInfo entity) {
        return createModelWithId(entity.getId(), entity, entity.getJobExecutionId());
    }

    @Override
    protected StepExecutionInfoResource instantiateModel(StepExecutionInfo entity) {
        return new StepExecutionInfoResource(entity.getStepExecution(), entity.getTimeZone());
    }
}
