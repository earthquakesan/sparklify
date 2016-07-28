Create View spo As
    Construct {
        ?s ?p ?o ; 
    }
    With
        ?s = uri(?subject)
        ?p = uri(?predicate)
        ?o = uri(?object)
    From
        spo

Create View splt As
    Construct {
        ?s ?p ?l ; 
    }
    With
        ?s = uri(?subject)
        ?p = uri(?predicate)
        ?l = plainLiteral(?literal)
    From
        splt
