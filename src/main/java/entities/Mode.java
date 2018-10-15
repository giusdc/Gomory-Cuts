package entities;

//only integer cuts: 0
//only fractional cuts: 1
//both integer and fractional cuts: 2
//single integer cut: 3
//single fractional cut: 4
//both single integer and single fractional cut: 5

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
