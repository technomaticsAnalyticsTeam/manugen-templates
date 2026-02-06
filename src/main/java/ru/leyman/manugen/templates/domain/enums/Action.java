package ru.leyman.manugen.templates.domain.enums;

public enum Action {

    /**
     * READ - just read
     * WRITE - read & write
     * SHARE - read & write & share
     * DELETE - read & write & delete & share
     * !!!НЕ МЕНЯТЬ ПОРЯДОК!!!
     */
    READ, WRITE, SHARE, DELETE

}
