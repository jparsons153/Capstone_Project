## Validation Report programmatic generator

#### Description:

Programmatically generates MS Word documents through Web page User Interface based on user input. Uses XML parsing (JAXB, DOCX4J) to update content controls in template file. Allows for various products and templates to be created and
persisted to a database for re-use.

#### Installation guide:

- create 'report_gen' database & grant user permissions
- set environmental variables for database connection

#### How to:
##### Set-up
- create defects.csv using schema ID,Defect name,Description,AQL (comma delimited)
- create document template
-   content control fields must be inserted in the template
- defects.csv & Template_DOCX.docx are available as samples in the project

##### Adding content controls to template
For the parser to work, content controls fields must exist in the template, these are available to insert using MS Word (note dev tools are not supported in web Office 365). The following content controls need to exist in the template (these are searched and parsed by XPath) - reportID, productSKU, tool, productCell, productSpec, processMap, validationStrategy (?)

`2024-10-11 10:25:58.835  INFO 17466 --- [nio-8080-exec-1] o.d.m.datastorage.BindingTraverserXSLT   : /validation_report[1]/productSKU[1]/productSpec[1]
 yielded result 'QS101'`

##### Creating a New Report
1. Go to '<host>/new' to enter the home screen
2. Enter the requested infomation
    a. ID - auto-populated
    b. Document Type - Select Document Type or 'Add new template', .docx & ?? formats are supported. Fields must be added to template using MS Word Dev tools
    c. PRODUCT SKU# - Select Product or Add new product. Currently must be an number. Add "Defects CSV file" for inspection data. Process map image may also be selected (currently this is required, if <image> field in template)
    d. Tool - insert tool number (must be a number)
    e. Validation Strategy - Select Validation strategy or create a new one. 'New Tool' validation strategy is available by default using 'Normal' inspection type and 'GL2' inspection level.
    f. Ensure all fields are populated before proceeding.
3. Click 'Auto Generate Report' - `.docx` file saved to browser

##### Available endpoints
1. `<host>/reportIndex` - lists all Reports created
2. `<host>/new` - create new report using form
3. `<host>/newTemplate` - add template file for report, note `fields` must be added following the correct naming convention.
4. `<host>/newProduct` - create new product using form, defects csv file and process map are required.
5. `<host>/newVal` - create new Validation Type, this uses data from the ISO standard to populate accept / reject allowables. Currently only `normal` and `GL2` are available.
