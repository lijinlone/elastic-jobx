/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package com.dangdang.example.elasticjob.core.main;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.job.api.listener.AbstractDistributeOnceElasticJobListener;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.dangdang.example.elasticjob.core.job.SequenceDataFlowJobDemo;
import com.dangdang.example.elasticjob.core.job.SimpleJobDemo;
import com.dangdang.example.elasticjob.core.job.ThroughputDataFlowJobDemo;

public final class JobMain {
    
    private final ZookeeperConfiguration zkConfig1 = new ZookeeperConfiguration("localhost:2181", "elasticjob-local1");
    
    private final CoordinatorRegistryCenter regCenter1 = new ZookeeperRegistryCenter(zkConfig1);

    private final ZookeeperConfiguration zkConfig2 = new ZookeeperConfiguration("localhost:2181", "elasticx-job-console");

    private final CoordinatorRegistryCenter regCenter2 = new ZookeeperRegistryCenter(zkConfig2);
    
    private final JobConfiguration jobConfig1 = new JobConfiguration("simpleElasticDemoJob", SimpleJobDemo.class, 10, "0/5 * * * * ?");
    
    private final JobConfiguration jobConfig2 = new JobConfiguration("throughputDataFlowElasticDemoJob", ThroughputDataFlowJobDemo.class, 10, "0/5 * * * * ?");
    
    private final JobConfiguration jobConfig3 = new JobConfiguration("sequenceDataFlowElasticDemoJob", SequenceDataFlowJobDemo.class, 10, "0/5 * * * * ?");

    private final JobConfiguration jobConfig4 = new JobConfiguration("SimpleJobDemo", SimpleJobDemo.class, 2, "0/20 * * * * ?");
    
    // CHECKSTYLE:OFF
    public static void main(final String[] args) {
    // CHECKSTYLE:ON
        new JobMain().init();
    }
    
    public void init() {
//        zkConfig.setNestedPort(4181);
//        zkConfig.setNestedDataDir(String.format("target/test_zk_data/%s/", System.nanoTime()));
        regCenter1.init();
        new JobScheduler(regCenter1, jobConfig1, new SimpleDistributeOnceElasticJobListener()).init();

//        regCenter2.init();
//        new JobScheduler(regCenter2, jobConfig4, new SimpleDistributeOnceElasticJobListener()).init();

//        new JobScheduler(regCenter2, jobConfig2).init();
//        new JobScheduler(regCenter2, jobConfig3).init();
    }
    
    class SimpleDistributeOnceElasticJobListener extends AbstractDistributeOnceElasticJobListener {
        
        SimpleDistributeOnceElasticJobListener() {
            super(1000L, 1000L);
        }
        
        @Override
        public void doBeforeJobExecutedAtLastStarted(final JobExecutionMultipleShardingContext shardingContext) {
            System.out.println("------ before simple job start ------");
        }
        
        @Override
        public void doAfterJobExecutedAtLastCompleted(final JobExecutionMultipleShardingContext shardingContext) {
            System.out.println("------ after simple job start ------");
        }
    }
}
