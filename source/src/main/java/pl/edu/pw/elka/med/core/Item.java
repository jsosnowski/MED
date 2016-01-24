package pl.edu.pw.elka.med.core;

/**
 * Implementacja itemu z bazy danych.
 */
public class Item {

    private final String type;
    private final String value;

    Item(String type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Zwraca typ itemu.
     *
     * @return typ itemu
     */
    public String getType() {
        return type;
    }

    /**
     * Zwraca wartość itemu.
     *
     * @return wartość itemu
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(type, item.type)
                .append(value, item.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(type)
                .append(value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("type", type)
                .append("value", value)
                .toString();
    }
}
