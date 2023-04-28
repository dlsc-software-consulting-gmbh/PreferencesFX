module com.dlsc.preferencesfx.demo {

    requires com.dlsc.preferencesfx;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j;
    requires com.google.gson;

    exports com.dlsc.preferencesfx.demo;

    opens com.dlsc.preferencesfx.demo;
    opens com.dlsc.preferencesfx.demo.extended;
}
