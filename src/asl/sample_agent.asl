// Agent sample_agent in project jason_basic_enterprise_integration

/* Initial beliefs and rules */
//interested_in(1, 2, sample_agent).
//interested_in(_,x,y).
interested_in(_, From, Subject,Body, User) :- relevant(User, From, Subject,Body).

//interested_in(_,x,y1).
//interested_in(y).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.");
                   .my_name(N);
                   externalSysCon.JdbcConnector(get_users,Users);
                   //todo this should be move to the plan that gets trigred when zookeeper node changes
                   externalSysCon.JdbcConnector(get_rules,RulesAsStrings);
                   for (.member(RuleAsString, RulesAsStrings)) {
    					 rules.add_rule(RuleAsString);
    					 //+RuleAsString;
   					};
  					rules.get_rules(_, R);
  					.print(R).
              