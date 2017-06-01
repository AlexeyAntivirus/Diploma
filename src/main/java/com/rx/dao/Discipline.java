package com.rx.dao;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Discipline {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Document> curriculums;

    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "disciplines")
    private Set<User> users;


    protected Discipline() {
    }

    private Discipline(DisciplineBuilder builder) {
        this.name = builder.name;
        this.curriculums = builder.curriculums;
    }

    public static DisciplineBuilder builder() {
        return new DisciplineBuilder();
    }

    public Set<Document> getCurriculums() {
        return curriculums;
    }

    public void setCurriculums(Set<Document> curriculums) {
        this.curriculums = curriculums;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @PreRemove
    private void removeDisciplinesFromUsers() {
        for (User u : users) {
            u.getDisciplines().remove(this);
        }
    }

    public static class DisciplineBuilder {
        private String name;

        private Set<Document> curriculums;


        private DisciplineBuilder() {
            this.curriculums = new HashSet<>();
        }

        public DisciplineBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DisciplineBuilder withCik(Document cik) {
            this.curriculums.add(cik);
            return this;
        }

        public Discipline build() {
            return new Discipline(this);
        }
    }
}
