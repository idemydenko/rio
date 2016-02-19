package org.fie.test

class Fencer {
    Long id
    String firstname
    String lastname
    String gender
    String weapon
    String nationality
    float total

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this.is(obj))
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Fencer other = (Fencer) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "F [" + id + ", " + firstname + " " + lastname + ", gender=" + gender + ", weapon=" + weapon + "]";
    }
 
    public String toCsv() {
        "\"${id}\",\"${firstname} ${lastname}\",${nationality},\"${gender}\",\"${weapon}\",\"${total}\","
    }   
}
