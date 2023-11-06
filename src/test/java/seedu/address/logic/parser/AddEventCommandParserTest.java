package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENTNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENTTYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddEventCommand;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        // dated event in user
        assertParseSuccess(parser, "user en/CS2103 Meeting h/2023-10-10 1030 1130 r/y",
                new AddEventCommand("CS2103 Meeting", "2023-10-10 1030 1130", "dated"));

        // dated event in friend
        assertParseSuccess(parser, "1 en/CS2103 Meeting h/2023-10-10 1030 1130 r/y",
                new AddEventCommand("CS2103 Meeting", Index.fromOneBased(1),
                        "2023-10-10 1030 1130", "y"));

    }

    @Test
    public void parse_repeatedValue_failure() {
        String validCommand = "user en/CS2103 Meeting h/2023-10-10 1030 1130 r/y";

        // multiple event name
        assertParseFailure(parser, validCommand + "en/CS2103 Meeting",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENTNAME));

        // multiple event schedule
        assertParseFailure(parser, validCommand + "h/2023-10-10 1030 1130",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHEDULE));

        // multiple event reminder
        assertParseFailure(parser, validCommand + "r/y",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMINDER));

        // multiple fields repeated
        assertParseFailure(parser, validCommand + "en/CS2103 Meeting" + "r/y",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENTNAME, PREFIX_REMINDER));

        // invalid value followed by valid value

        // invalid schedule
        assertParseFailure(parser, "h/2023-10-10 1030 1145" + validCommand,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHEDULE));

        //invalid reminder
        assertParseFailure(parser, "r/yes" + validCommand,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMINDER));

        //valid value followed by invalid value

        // invalid schedule
        assertParseFailure(parser, validCommand + "h/2023-10-10 1030 1145",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHEDULE));

        //invalid reminder
        assertParseFailure(parser, validCommand + "r/yes",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMINDER));
    }

    @Test
    public void parse_fieldMissing_failure() {
        String expectedMessage = String.format("Command format is invalid! \n"
                + AddEventCommand.MESSAGE_USAGE);

        String expectedMessage2 = String.format("Invalid index!\n"
                + "Index can only be 'user' or a 'positive integer!' \n");

        // missing event name prefix
        assertParseFailure(parser, "user h/2023-10-10 1030 1130 r/y",
                expectedMessage);

        // missing event schedule prefix
        assertParseFailure(parser, "user en/CS2103 Meeting r/y",
                expectedMessage);

        // missing event reminder prefix
        assertParseFailure(parser, "user en/CS2103 Meeting h/2023-10-10 1030 1130",
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, "user CS2103 Meeting 2023-10-10 1030 1130 y",
                expectedMessage);

        // wrong index
        assertParseFailure(parser, "wrong type/dated en/CS2103 Meeting h/2023-10-10 1030 1130 r/y",
                expectedMessage2);
    }

}
