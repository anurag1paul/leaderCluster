/*
 * This file is part of the LeaderCluster distribution.
 * Copyright (c) 2017 Delhivery India Pvt. Ltd.
 *
 * LeaderCluster is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, version 3.
 *
 * LeaderCluster is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.delhivery.clustering.spatialClustering;

import com.delhivery.clustering.algorithm.Clusterable;
import com.delhivery.clustering.utils.Coordinate;

/**
 * @author Anurag Paul(anurag.paul@delhivery.com)
 *         Date: 4/1/17
 */
public class SpatialPoint implements Clusterable<SpatialPoint> {

    private Coordinate coordinate;
    private double weight = 1.0;

    public SpatialPoint(Coordinate coordinate, double weight){
        this.coordinate = coordinate;
        this.weight = weight;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(SpatialPoint spatialPoint) {
        if (coordinate.compareTo(spatialPoint.coordinate) == 0)
            return 0;
        else {
            return new Double(weight).compareTo(spatialPoint.weight);
        }
    }

    @Override
    public String toString() {
        return String.format("{ Coordinate: %s, Weight: %f }", coordinate.toString(), weight);
    }
}
