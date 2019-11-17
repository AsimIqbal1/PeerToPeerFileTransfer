public class Connections {



    private Integer[] peer01Connection = {2,3,5};
    private Integer[] peer02Connection = {4,5};
    private Integer[] peer03Connection = {1,4,5};
    private Integer[] peer04Connection = {2,3};
    private Integer[] peer05Connection = {1,2,3};

    private Integer[] peer01ConnectionPorts = {2222,3333,5555};
    private Integer[] peer02ConnectionPorts = {1111,4444,5555};
    private Integer[] peer03ConnectionPorts = {1111,4444,5555};
    private Integer[] peer04ConnectionPorts = {2222,3333};
    private Integer[] peer05ConnectionPorts = {1111,2222,3333};


//    private Integer[] peer01ConnectionPorts = {2222};
//    private Integer[] peer02ConnectionPorts = {1111,5555};
//    private Integer[] peer03ConnectionPorts = {1111};
//    private Integer[] peer04ConnectionPorts = {3333};
//    private Integer[] peer05ConnectionPorts = {2222};

    String getPath() {
        return "F:\\CN\\Project\\P2PFileTransfer# ";
    }

    Integer[] getPeer01ConnectionPorts() {
        return peer01ConnectionPorts;
    }

    Integer[] getPeer02ConnectionPorts() {
        return peer02ConnectionPorts;
    }

    Integer[] getPeer03ConnectionPorts() {
        return peer03ConnectionPorts;
    }

    Integer[] getPeer04ConnectionPorts() {
        return peer04ConnectionPorts;
    }

    Integer[] getPeer05ConnectionPorts() {
        return peer05ConnectionPorts;
    }

    Integer[] getPeer01Connection() {
        return peer01Connection;
    }

    Integer[] getPeer02Connection() {
        return peer02Connection;
    }

    Integer[] getPeer03Connection() {
        return peer03Connection;
    }

    Integer[] getPeer04Connection() {
        return peer04Connection;
    }

    Integer[] getPeer05Connection() {
        return peer05Connection;
    }
}
