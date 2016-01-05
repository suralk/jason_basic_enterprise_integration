// Agent sample_agent in project jason_basic_enterprise_integration

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.");externalSysCon.JdbcConnector(1).
