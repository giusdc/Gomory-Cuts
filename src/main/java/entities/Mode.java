package entities;

public enum Mode {
    integer, fractional, integerAndFractional, singleInteger, singleFractional, singleIntegerAndFractional;

    @Override
    public String toString() {
        String name = null;
        switch (this) {
            case integer:
                name = "Integer cuts";
                break;
            case fractional:
                name = "Fractional cuts";
                break;
            case integerAndFractional:
                name = "Integer and fractional cuts";
                break;
            case singleInteger:
                name = "Integer cut";
                break;
            case singleFractional:
                name = "Fractional cut";
                break;
            case singleIntegerAndFractional:
                name = "Integer and fractional cut";
                break;
        }
        return name;
    }
}
