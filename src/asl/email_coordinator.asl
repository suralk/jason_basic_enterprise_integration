// Agent sample_agent in project jason_basic_enterprise_integration

/* Initial beliefs and rules */
timeout(5000). // milliseconds
last_query_number(0).
num_workers(10).
workers([agent1]).
/* Initial goals */

!start.

/* Plans */

+!start : true <- //externalSysCon.SendReceiveEmail(1,EmailIds);
				//print(EmailIds);
				?last_query_number(QN);
				QN1 = QN+1;
				-+ last_query_number(QN1);
				// The query number isn’t used by the worker agent, but we want
   				 // it to send it back with its answer
   				//+recommended_agents(QN1, []);
   				+answered(QN1, []);
    			.broadcast(askAll, interested_in(QN1, _	,_, _,User)); // Agent is a new variable here
    			?timeout(TO);
    			.wait({ +all_answers_received(QN1) },TO);    			
    			.findall(User, interested_in(QN1, _, _,_,User), Users).
    			
+interested_in(QueryId,_,_,_,Answer)[source(Worker)] : workers(Workers) <- 
    .print(Answer);   
    ?answered(QueryId, AnsweredSoFar);
     .union(AnsweredSoFar, [Worker], AnsweredNow);
    -+answered(QueryId, AnsweredNow);
    .length(AnsweredNow, NumAnswered);
    .length(Workers, NumAnswered); // This could be precalculated when workers(ListOfWorkers) is asserted, and stored as a separate belief
     if (NumAnswered == NumAnswered) {     	
        +all_answers_received(QueryId);
    }.

