package default_group.file_transfer_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Ipv4AddressLister
{
    public static ObservableList<String> getObservableListOfAllIpv4Addresses() throws SocketException
    {
        ObservableList<String> observableListOfIpv4Addresses = FXCollections.observableArrayList();
        Enumeration<NetworkInterface> allNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetworkInterfaces.hasMoreElements())
        {
            NetworkInterface currentInterface = allNetworkInterfaces.nextElement();
            if (!currentInterface.isLoopback())
            {
                addIpv4AddressesForCurrentNetworkInterfaceToList(currentInterface, observableListOfIpv4Addresses);
            }
        }
        return observableListOfIpv4Addresses;
    }


    private static void addIpv4AddressesForCurrentNetworkInterfaceToList(NetworkInterface currentInterface, ObservableList<String> observableListOfIpv4Address)
    {
        Enumeration<InetAddress> currentNetworkInterfaceInetAddresses = currentInterface.getInetAddresses();
        while (currentNetworkInterfaceInetAddresses.hasMoreElements())
        {
            InetAddress currentInetAddress = currentNetworkInterfaceInetAddresses.nextElement();
            if (currentInetAddress instanceof Inet4Address)
            {
                observableListOfIpv4Address.add(currentInetAddress.toString());
            }
        }
    }
}