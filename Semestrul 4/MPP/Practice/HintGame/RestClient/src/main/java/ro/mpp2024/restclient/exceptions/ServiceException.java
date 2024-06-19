package ro.mpp2024.restclient.exceptions;

public class ServiceException extends RuntimeException {
    public ServiceException(Exception e) {
        super(e);
    }
    public ServiceException(String message) {
        super(message);
    }
}
