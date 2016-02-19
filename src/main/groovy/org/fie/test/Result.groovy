package org.fie.test

import groovy.transform.Sortable;

class Result implements Comparable<Result>{
    Fencer fencer
    Long real_competition_id
    Long real_season_id
    Integer rank
    Float point
    String compName
    String federation
    String type
    Date date
    @Override
    public int compareTo(Result o) {
        if (type == 'CHM') {
            return -1
        }
        
        if (o.type == 'CHM') {
            return 1
        }
        
        if (type == 'CHZ') {
            return -1
        }
        
        if (o.type == 'CHZ') {
            return 1
        }

        return -(point <=> o.point);
    }
   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fencer == null) ? 0 : fencer.hashCode());
        result = prime * result + ((real_competition_id == null) ? 0
                : real_competition_id.hashCode());
        result = prime * result
                + ((real_season_id == null) ? 0 : real_season_id.hashCode());
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
        Result other = (Result) obj;
        if (fencer == null) {
            if (other.fencer != null)
                return false;
        } else if (!fencer.equals(other.fencer))
            return false;
        if (real_competition_id == null) {
            if (other.real_competition_id != null)
                return false;
        } else if (!real_competition_id.equals(other.real_competition_id))
            return false;
        if (real_season_id == null) {
            if (other.real_season_id != null)
                return false;
        } else if (!real_season_id.equals(other.real_season_id))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "R [r: " + rank + "; p: " + point + "; " + compName + "; " + type + "; " + date + "]";
    }

    def String toCsv() {
        "\"${compName}\",\"${type}\",\"${rank}\",\"${point}\","
    }
      
}
