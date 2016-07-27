package iboltz.expatmig.utils;

import java.text.SimpleDateFormat;

public class AppConstants {
    public static final String APPId = "df4c4287b57f4e1ab7920ca85a106498";

    public static final String PrefsStorage = "SDPrefs";
    public static final String GcmProjectID = "335335578624";
    public static final String GcmApiKey = "AIzaSyCUTgeDP15tNKtvbI04uo2bNK1BGG2E7mw";

    public static final String MessageKey = "message";

    public static final String ServerTimeZone = "America/Kentucky/Louisville";
    public static final SimpleDateFormat StandardDateFormat = new SimpleDateFormat(
            "dd-MMM-yyyy hh:mm aa");
    public static final SimpleDateFormat JsonDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss");


    public enum LocalCacheType {
        Tables(0),
        MyRestaurant(1),
        Restaurants(2),
        DishCategories(3),
        SpokenKitchenOrders(4),
        AllRestaurants(5),
        PaymentTypes(6),
        CurrentUser(7),
        PrinterSettings(8);

        public int value;

        LocalCacheType(int value) {
            this.value = value;
        }
    }

    public enum PrintTextAlignment {
        Left,
        Right
    }

    public enum OrderStatus {
        Pending(1),
        Acknowledge(2),
        FoodReady(3),
        BillReady(4),
        Void(5),
        BillPrinted(6),
        OnlineOrder_intialize(7),
        OnlineOrder_CashReceived(8),
        PayLater(9);

        public int value;

        OrderStatus(int value) {
            this.value = value;
        }
        }

    public enum ObjectState {
        UnChanged(0),
        Added(1),
        Modified(2),
        Deleted(3);
        public int value;

        ObjectState(int value) {
            this.value = value;
        }
    }

    public enum PaymentTypes {
        Cash(1),
        CreditCard(2),
        DebitCard(3);
        public int value;

        PaymentTypes(int value) {
            this.value = value;
        }
    }
    public enum PrinterColCount {
        PrinterCol1Count(4),
        PrinterCol2Count(28),
        PrinterCol3Count(8),
        PrinterTotalCount(40);
        public int value;

        PrinterColCount(int value) {
            this.value = value;
        }
    }

    public enum BillRound {
        BillRound5CentFloor(1);
        public int value;

        BillRound(int value) {
            this.value = value;
        }
    }

}

