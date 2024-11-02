
public class App {
    public static void main(String[] args) throws Exception {
        // Login instance = new Login();
        // Register regInstance = new Register();

        // ApplianceList al = new ApplianceList(new User("techie410",
        // User.PERMISSION_CLIENT));

        // ApplianceListIndividualButton alib = new ApplianceListIndividualButton(
        // new User("techie410", User.PERMISSION_CLIENT));

        // ApplianceList alib = new ApplianceList(
        // new User("techie410", User.PERMISSION_CLIENT));

        // testImage hairdryer = new testImage();

        // Appliance hairdryerapp = new Appliance(1);

        // System.out.println(hairdryerapp.checkApplianceIDExists()); // checking if it
        // exists

        // System.out.println(hairdryerapp.checkApplianceIDExists());
        // ApplianceInfo hairdryerInfo = new ApplianceInfo(hairdryerapp);

        // new ApplianceEditor(hairdryerapp);

        // SQLRequest sqlr = new SQLRequest();
        // System.out.println(sqlr.nextID());

        // new newAppliance(new User("techie410", User.PERMISSION_CLIENT));

        User me = new User("techie410", User.PERMISSION_CLIENT);
        // AppointmentList apl = new AppointmentList(me);

        // new AppointmentBooking();

        new ApplianceList(me);

    }
}
