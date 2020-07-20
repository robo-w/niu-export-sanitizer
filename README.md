# Export Sanitizer for NIU

This tool checks for unrecognized telephone number formats in the CSV to import into CoCeSo.
The `RunChecks` class takes a single CSV file as parameter to validate.

On the output the lines with unrecognized number formats are shown.
The CSV can then be adapted, to re-run the program and check again for violations.

To run the program a CoCeSo 2.4.4 or higher needs to be available in the local maven repository.
