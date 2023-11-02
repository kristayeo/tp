package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.timetable.Schedule;

/**
 * Represents a command to add a non-recurring event to the calendar.
 * The event can either be a dated event or a meetup event.
 * A dated event is an event that is added to a friend's schedule or the user's schedule.
 * A meetup event is an event that is added to the user's schedule and involves meeting up with a friend.
 * Inherits from the Command class.
 */
public class AddEventCommand extends Command {

    public static final String COMMAND_WORD = "addevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Adds a non-recurring event to the calendar.\n"
        + "Parameters: "
        + "INDEX "
        + "type/EVENT_TYPE "
        + "en/EVENT_NAME "
        + "h/[Date [YYYY-MM-DD] StartTime (HHMM) EndTime (HHMM)] "
        + "r/[REMINDER: y/n] \n"
        + "Example: " + COMMAND_WORD + " "
        + "1 "
        + "type/dated "
        + "en/CS2103T Lecture "
        + "h/2020-03-02 1400 1600 "
        + "r/y \n"
        + "Note: If you are adding a meetup event, "
        + "then index refers to the index of the friend you are meeting with. \n"
        + "If you are adding a dated event, then index should be the index of "
        + "the friend you are adding the dated event to or 'user' "
        + "if you would like to add the event to yourself \n";

    public static final String MESSAGE_SUCCESS = "New event added: ";

    private final String eventName;
    private final String eventType;
    private final Index index;
    private final String schedule;
    private final String reminder;

    /**
     * Constructs an AddEventCommand object with the specified event name, index, schedule,
     * reminder and event type.
     * @param eventName
     * @param index
     * @param schedule
     * @param reminder
     * @param eventType
     */
    public AddEventCommand(String eventName, Index index, String schedule,
        String reminder, String eventType) {

        requireAllNonNull(schedule);

        this.eventName = eventName;
        this.schedule = schedule;
        this.index = index;
        this.reminder = reminder;
        this.eventType = eventType.toLowerCase();
    }

    /**
     * Constructs an AddEventCommand object with the specified event name, schedule,
     * reminder and event type.
     * @param eventName
     * @param schedule
     * @param reminder
     * @param eventType
     */
    public AddEventCommand(String eventName, String schedule,
        String reminder, String eventType) {

        this(eventName, null, schedule, reminder, eventType);
    }

    /**
     * When successfully executed, it should add a new event to the user's timetable.
     * @param model {@code Model} which the command should operate on.
     * @return A command result in the form of a string.
     * @throws CommandException If the command is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person friend;

        try {
            if (this.index == null) {
                friend = model.getUser();
            } else {
                friend = model.getFilteredPersonList().get(index.getZeroBased());
            }

            Schedule friendSchedule = friend.getSchedule();

            switch (eventType) {
            case "dated":
                friendSchedule.addDatedEvent(eventName + " " + schedule + " " + reminder);
                return new CommandResult(MESSAGE_SUCCESS + "\nDated Event:\n" + eventName + " "
                        + schedule + " to " + friend.getName(), false, false, true, false);
            default:
                throw new CommandException("Invalid event type!"
                    + "\n Event type can only be 'Dated' or 'Meetup'");
            }
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

}