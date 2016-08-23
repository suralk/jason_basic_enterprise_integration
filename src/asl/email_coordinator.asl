// Agent sample_agent in project jason_basic_enterprise_integration

/* Initial beliefs and rules */
timeout(5000). // milliseconds
last_query_number(0).
num_workers(10).
workers([agent1]).
/* Initial goals */

!start.

/* Plans */

+!start  <-     .wait(5000);
                //externalSysCon.SendReceiveEmail(1,EmailIds);
				//print(EmailIds);
				?last_query_number(QN);
				QN1 = QN+1;
				-+ last_query_number(QN1);
				// The query number isn’t used by the worker agent, but we want
   				 // it to send it back with its answer
   				//+recommended_agents(QN1, []);
   				+answered(QN1, []);
    			.broadcast(askAll, interested_in(QN1, "Great Uncle Sylvester Cranefield", "", "", User));
    			?timeout(TO);
    			.wait(all_answers_received(QN1),TO,EventTime);
    			.print("Elapsed time in wait: ", EventTime);    			
    			.findall(User, interested_in(QN1, _, _,_,User), Users);
    			.print("Interested users: ", Users).

+interested_in(QueryId,_,_,_,Answer)[source(Worker)] <- 
    .print("Answer received from: ", Answer);   
    ?answered(QueryId, AnsweredSoFar);
     .union(AnsweredSoFar, [Worker], AnsweredNow);
    -+answered(QueryId, AnsweredNow);
    .length(AnsweredNow, NumAnswered);
    ?workers(Workers);
    .length(Workers, NumToAnswer); // This could be precalculated when workers(ListOfWorkers) is asserted, and stored as a separate belief
     if (NumAnswered == NumToAnswer) {     	
        +all_answers_received(QueryId);
    }.
