
import java.sql.*;

/*
RULES
ID: INTEGER(10) NOT NULL
RULE: VARCHAR(500)
USERNAME: VARCHAR(20)

USERS
ID: INTEGER(10) NOT NULL
USERNAME: VARCHAR(20)

This is the data in the tables (I typed it here by hand as my old laptop can no longer connect to the network). I can’t remember whether the ‘role’ rules (which are actually facts) were ever used in the Jason code, but they could be queried by ‘relevant’ rules. The username _global will match any user within Jason as it is an anonymous variable.

RULES:
ID            RULE      USERNAME
1              relevant(crast78p, From, _) :- .substring(‘Cranefield’, From)        crast78p
2              role(crast78p, manage, project1)              _global
3              role(bob, minion, project1)         _global

USERS:
ID            USERNAME
1              a
2              b
3              c
4              d
5              e
6              f
7              crast78p
*/

// Based on http://db.apache.org/derby/docs/10.11/getstart/rwwdactivity3.html, http://svn.apache.org/repos/asf/db/derby/code/trunk/java/demo/workingwithderby/WwdEmbedded.java
public class InitDB {

    public static void main(String[] args) {
        // define the driver to use 
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        // the database name  
        String dbName = "basic_enterprise_integration";
        // define the Derby connection URL to use 
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";

        Connection conn = null;
        Statement s;
        PreparedStatement psInsert;
        ResultSet rules;
        String printLine = "  __________________________________________________";
        String dropTableRules = "DROP TABLE RULES";
        String dropTableUsers = "DROP TABLE USERS";
        String createTableRules = "CREATE TABLE RULES  "
                + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                + " RULE VARCHAR(500) NOT NULL, "
                + " USERNAME VARCHAR(20) NOT NULL)";
        String createTableUsers = "CREATE TABLE USERS  "              
                + "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                + " USERNAME VARCHAR(20) NOT NULL)";
        String insertUsers = "INSERT INTO USERS(USERNAME) VALUES "
                + " ('crast78p'), ('bob')";
        String insertRules = "INSERT INTO RULES(RULE,USERNAME) VALUES "
                + " ('relevant(crast78p, From, _) :- .substring(''Cranefield'', From)', 'crast78p'), "
                + " ('role(crast78p, manage, project1)', '_global'), "
                + " ('role(bob, minion, project1)', '_global')";
        String answer;

        //  JDBC code sections   
        //  Beginning of Primary DB access section
        try {
            // Create (if needed) and connect to the database.
            // The driver is loaded automatically.
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected to database " + dbName);
            // Use of tableExists is from https://wpollock.com/AJava/DerbyDemo.htm
            if ( tableExists( conn, "USERS" ) )  {
                System.out.println ( "Dropping tables USERS and RULES" );
                try {
                  s = conn.createStatement();
                  s.execute( dropTableUsers );
                  //s.close();
                  //s = conn.createStatement();
                  s.execute( dropTableRules );
                  s.close();                  
                } catch ( SQLException e ) {
                  String theError = e.getSQLState();
                  System.out.println( "Can't drop tables" );
                  System.exit(1);
                }
            }
            // Batched statement based on http://stackoverflow.com/questions/10929369/how-to-execute-multiple-sql-statements-from-java
            s = conn.createStatement();
            s.addBatch(createTableUsers);
            s.addBatch(createTableRules);
            s.addBatch(insertUsers);
            s.addBatch(insertRules);
            s.executeBatch();

            //  Prepare the insert statement to use 
            //psInsert = conn.prepareStatement("insert into USERS(USERNAME) values (?)");

            //Insert a row into the USERS table
            //psInsert.setString(1, "Test entry");
            //psInsert.executeUpdate();

            //   Select all records in the RULES table
            rules = s.executeQuery("select RULE, USERNAME from RULES");
            
            //  Loop through the ResultSet and print the data 
            System.out.println(printLine);
            while (rules.next()) {
                System.out.println("Rule:  " + rules.getString(1) + " (User: " + rules.getString(2) + ")");
            }
            System.out.println(printLine);
            //  Close the resultSet 
            rules.close();

            //psInsert.close();
            s.close();
            conn.close();

            /**
             * * In embedded mode, an application should shut down Derby.
             * Shutdown throws the XJ015 exception to confirm success. **
             */
            if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
                boolean gotSQLExc = false;
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                    if (se.getSQLState().equals("XJ015")) {
                        gotSQLExc = true;
                    }
                }
                if (!gotSQLExc) {
                    System.out.println("Database did not shut down normally");
                } else {
                    System.out.println("Database shut down normally");
                }
            }

        } catch (Throwable e) {
            /*       Catch all exceptions and pass them to 
             *       the Throwable.printStackTrace method  */
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
    }

    public static boolean wwdChk4Tables(Connection conTst) throws SQLException {
        boolean chk = true;
        boolean doCreate = false;
        try {
            Statement s = conTst.createStatement();
            s.execute("update USERS set ENTRY_DATE = CURRENT_TIMESTAMP, WISH_ITEM = 'TEST ENTRY' where 1=3");
        } catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            //   System.out.println("  Utils GOT:  " + theError);
            /**
             * If table exists will get - WARNING 02000: No row was found *
             */
            if (theError.equals("42X05")) // Table does not exist
            {
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                System.out.println("WwdChk4Table: Incorrect table definition. Drop tables and rerun this program");
                throw sqle;
            } else {
                System.out.println("WwdChk4Table: Unhandled SQLException");
                throw sqle;
            }
        }
        //  System.out.println("Just got the warning - table exists OK ");
        return true;
    }
    /**
     * * END wwdInitTable  *
     */

    // From https://wpollock.com/AJava/DerbyDemo.htm    
    // Derby doesn't support the standard SQL views.  To see if a table
    // exists you normally query the right view and see if any rows are
    // returned (none if no such table, one if table exists).  Derby
    // does support a non-standard set of views which are complicated,
    // but standard JDBC supports a DatabaseMetaData.getTables method.
    // That returns a ResultSet but not one where you can easily count
    // rows by "rs.last(); int numRows = rs.getRow()".  Hence the loop.

    private static boolean tableExists ( Connection con, String table ) {
      int numRows = 0;
      try {
        DatabaseMetaData dbmd = con.getMetaData();
        // Note the args to getTables are case-sensitive!
        ResultSet rs = dbmd.getTables( null, "APP", table.toUpperCase(), null);
        while( rs.next() ) ++numRows;
      } catch ( SQLException e ) {
          String theError = e.getSQLState();
          System.out.println("Can't query DB metadata: " + theError );
          System.exit(1);
      }
      return numRows > 0;
    }

    
    
}
