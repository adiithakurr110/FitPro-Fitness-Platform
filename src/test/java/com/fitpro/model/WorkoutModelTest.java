package com.fitpro.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WorkoutModelTest {

    @Test
    @DisplayName("Workout factory creates correct polymorphic subclasses")
    public void testFactoryCreation() {
        Workout cardio = Workout.of("CARDIO");
        Workout strength = Workout.of("STRENGTH");
        Workout yoga = Workout.of("YOGA");
        Workout defaultWorkout = Workout.of("UNKNOWN_TYPE");
        Workout nullTypeWorkout = Workout.of(null);

        Assertions.assertTrue(cardio instanceof CardioWorkout);
        Assertions.assertEquals("CARDIO", cardio.getType());

        Assertions.assertTrue(strength instanceof StrengthWorkout);
        Assertions.assertEquals("STRENGTH", strength.getType());

        Assertions.assertTrue(yoga instanceof YogaWorkout);
        Assertions.assertEquals("YOGA", yoga.getType());

        Assertions.assertTrue(defaultWorkout instanceof CardioWorkout);
        Assertions.assertTrue(nullTypeWorkout instanceof CardioWorkout);
    }

    @Test
    @DisplayName("CardioWorkout calculates base MET calories correctly")
    public void testCardioCalorieCalculation() {
        Workout cardio = new CardioWorkout();
        cardio.setMetValue(8.0);
        double weight = 70.0;
        int minutes = 60;

        // Formula: MET * 3.5 * weight / 200 * minutes = 8.0 * 3.5 * 70 / 200 * 60 = 588.0
        double expected = 8.0 * 3.5 * 70.0 / 200.0 * 60.0;
        double actual = cardio.caloriesBurned(weight, minutes);

        Assertions.assertEquals(expected, actual, 0.001);
    }

    @Test
    @DisplayName("StrengthWorkout adds 10% EPOC afterburn factor")
    public void testStrengthCalorieCalculation() {
        Workout strength = new StrengthWorkout();
        strength.setMetValue(5.0);
        double weight = 80.0;
        int minutes = 45;

        // Base: 5.0 * 3.5 * 80 / 200 * 45 = 315.0. EPOC bonus: 315.0 * 1.10 = 346.5
        double base = 5.0 * 3.5 * 80.0 / 200.0 * 45.0;
        double expected = base * 1.10;
        double actual = strength.caloriesBurned(weight, minutes);

        Assertions.assertEquals(expected, actual, 0.001);
    }

    @Test
    @DisplayName("YogaWorkout applies 0.90 low intensity factor")
    public void testYogaCalorieCalculation() {
        Workout yoga = new YogaWorkout();
        yoga.setMetValue(3.0);
        double weight = 60.0;
        int minutes = 30;

        // Base: 3.0 * 3.5 * 60 / 200 * 30 = 94.5. Low intensity: 94.5 * 0.90 = 85.05
        double base = 3.0 * 3.5 * 60.0 / 200.0 * 30.0;
        double expected = base * 0.90;
        double actual = yoga.caloriesBurned(weight, minutes);

        Assertions.assertEquals(expected, actual, 0.001);
    }

    @Test
    @DisplayName("Workout getters and setters behave consistently")
    public void testGettersAndSetters() {
        Workout w = Workout.of("STRENGTH");
        w.setId(42);
        w.setTitle("Core Power");
        w.setDescription("Intense abdominal training");
        w.setLevel("INTERMEDIATE");
        w.setDurationMin(40);
        w.setMetValue(6.5);
        w.setTrainerId(5);
        w.setTrainerName("Coach Mike");

        Assertions.assertEquals(42, w.getId());
        Assertions.assertEquals("Core Power", w.getTitle());
        Assertions.assertEquals("Intense abdominal training", w.getDescription());
        Assertions.assertEquals("INTERMEDIATE", w.getLevel());
        Assertions.assertEquals(40, w.getDurationMin());
        Assertions.assertEquals(6.5, w.getMetValue());
        Assertions.assertEquals(5, w.getTrainerId());
        Assertions.assertEquals("Coach Mike", w.getTrainerName());
    }
}
