package org.fie.test

import groovy.sql.Sql

final String HEADER = '"real_fencer_id","Full name", "Gender","Weapon","Total"\n'

final String URL = "jdbc:mysql://localhost:3333/dev_fie"
final String USER = "dev_fie"
final String PASSWORD = "dev_fie"
final String DRIVER = "com.mysql.jdbc.Driver"


def query_template = """
    SELECT
        res.real_fencer_id as id,
        res.real_competition_id as compId,
        res.real_season_id as seasonId,
        a.lastname as lastname,
        a.firstname as firstname,
        a.Nationality,
        res.final as rank,
        res.point,
        c.CompName as compName,
        c.FedId as federation,
        c.CompCatId as type,
        com.WeaponId as weapon,
        com.GenderId as gender,
        com.FencCatId as category,
        c.datebegin 
    FROM 
        tbl_tournament_result res
        join Addresses a on res.real_fencer_id = a.addrId
        join Calendars c on c.CompId = res.real_competition_id
        left outer join Competitions com on com.CompId = res.real_competition_id
    where 
        c.datebegin>'2015-04-01' 
        and res.real_season_id=c.cpyear 
        and c.CompCatId<>'NF'
        and com.FencCatId='s' 
        and com.GenderId=?
        and com.WeaponId=? 
        and com.CompTypeId=?
    order by 
        a.Nationality asc,
        a.lastname asc,
        a.firstname asc,
        res.point desc
"""


// def file_template = "rioQ_${type}_${weapon}_${gender}.csv"

def genders = ['F', 'M']
def weapons = ['S', 'E', 'F']
def types = ['E', 'I']



def sql = Sql.newInstance(URL, USER, PASSWORD, DRIVER)

[genders, weapons, types].eachCombination {  params ->
    def results = [:]

    sql.eachRow(query_template, params) { row ->
        Fencer f = new Fencer();
        f.id = row.id
        f.firstname = row.firstname
        f.lastname = row.lastname
        f.nationality = row.Nationality
        f.gender = row.gender
        f.weapon = row.weapon

        Result r = new Result();
        r.fencer = f
        r.compName = row.CompName
        r.date = row.datebegin
        r.federation = row.Federation
        r.real_competition_id = row.compId
        r.real_season_id = row.seasonId
        r.rank = row.rank
        r.point = row.point
        r.type = row.type

        if (results[f] == null) {
            results[f] = []
        }
        results[f] << r
    }
    
    results.each { Fencer k, List<Result> v ->
        v.sort()
        
        int remain = 7
        
        if (v.any { it.type == 'CHM' })
            remain--;
             
        if (v.any { it.type == 'CHZ' })
            remain--;
        
        v = v[0..Math.min(v.size(), remain) - 1]
        
        k.total = v.sum { it.point }
        
    }
    
    def sorted = results.sort { e1, e2 ->
        -(e1.key.total <=> e2.key.total)
    }
    
    def out = new File("rioQ_${params[2]}_${params[1]}_${params[0]}.csv")
    if (out.exists()) {
        out.delete()
    }
    
    out << HEADER
    
    sorted.each { k, v ->
        out << k.toCsv()
        
        def sr = v.sort { e1, e2 ->
            e1.rank <=> e2.rank
        }
       
        sr.each { r ->
            out << r.toCsv()
            print r.toCsv()
        }
        out << "\n"
        println ""
    }
    
}