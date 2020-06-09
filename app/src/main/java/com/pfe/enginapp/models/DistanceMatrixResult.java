package com.pfe.enginapp.models;

import java.util.List;

public class DistanceMatrixResult {


    List<Row> rows;

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public class Row {
        List<Element> elements;

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }
    }

    public class Element{
        SubElement distance,duration;
        String status;

        public SubElement getDistance() {
            return distance;
        }

        public void setDistance(SubElement distance) {
            this.distance = distance;
        }

        public SubElement getDuration() {
            return duration;
        }

        public void setDuration(SubElement duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    public class SubElement {
        String text = "N/A";
        long value = 0;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }
}
