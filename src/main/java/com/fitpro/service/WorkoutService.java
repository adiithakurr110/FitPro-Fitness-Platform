package com.fitpro.service;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.model.Workout;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/** Business rules for the workout catalogue and enrollments. */
public class WorkoutService {
    private static final List<String> TYPES = Arrays.asList("CARDIO", "STRENGTH", "YOGA");
    private static final List<String> LEVELS = Arrays.asList("BEGINNER", "INTERMEDIATE", "ADVANCED");

    private final WorkoutDAO workoutDAO = new WorkoutDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    /** All workouts, or only one category when {@code type} is valid. */
    public List<Workout> list(String type) throws AppException {
        return (type != null && TYPES.contains(type.toUpperCase()))
                ? workoutDAO.findByType(type.toUpperCase())
                : workoutDAO.findAll();
    }

    public Set<Integer> enrolledIds(int userId) throws AppException {
        return enrollmentDAO.findEnrolledIds(userId);
    }

    public List<Workout> enrolledWorkouts(int userId) throws AppException {
        return enrollmentDAO.findEnrolledWorkouts(userId);
    }

    public void enroll(User user, int workoutId) throws AppException {
        if (user.isTrainer()) throw new AppException("Trainers cannot enroll in programs.");
        if (!workoutDAO.findById(workoutId).isPresent()) throw new AppException("Workout not found.");
        if (!enrollmentDAO.enroll(user.getId(), workoutId)) throw new AppException("You are already enrolled in this program.");
        NotificationService.getInstance().sendAsync(user.getEmail(), "You joined a new program. Let's go!");
    }

    /** Trainer-only: validates and publishes a new program. */
    public void publish(User trainer, String title, String description, String type,
                        String level, String durationText, String metText) throws AppException {
        if (!trainer.isTrainer()) throw new AppException("Only trainers can publish programs.");
        if (title == null || title.trim().length() < 3) throw new AppException("Title must be at least 3 characters.");
        if (type == null || !TYPES.contains(type.toUpperCase())) throw new AppException("Choose a valid type.");
        if (level == null || !LEVELS.contains(level.toUpperCase())) throw new AppException("Choose a valid level.");

        int duration;
        double met;
        try {
            duration = Integer.parseInt(durationText.trim());
            met = Double.parseDouble(metText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new AppException("Duration and intensity must be numbers.");
        }
        if (duration < 5 || duration > 240) throw new AppException("Duration must be between 5 and 240 minutes.");
        if (met < 1 || met > 15) throw new AppException("Intensity (MET) must be between 1 and 15.");

        Workout w = Workout.of(type);        // polymorphic creation
        w.setTitle(title.trim());
        w.setDescription(description == null ? "" : description.trim());
        w.setLevel(level.toUpperCase());
        w.setDurationMin(duration);
        w.setMetValue(met);
        w.setTrainerId(trainer.getId());
        workoutDAO.create(w);
    }
}
