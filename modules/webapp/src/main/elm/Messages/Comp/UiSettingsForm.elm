module Messages.Comp.UiSettingsForm exposing (Texts, gb)

import Data.Color exposing (Color)
import Data.Fields exposing (Field)
import Messages.Basics
import Messages.Data.Color
import Messages.Data.Fields


type alias Texts =
    { basics : Messages.Basics.Texts
    , general : String
    , showSideMenuByDefault : String
    , uiLanguage : String
    , itemSearch : String
    , maxResultsPerPageInfo : Int -> String
    , maxResultsPerPage : String
    , showBasicSearchStatsByDefault : String
    , enablePowerSearch : String
    , itemCards : String
    , maxNoteSize : String
    , maxNoteSizeInfo : Int -> String
    , sizeOfItemPreview : String
    , cardTitlePattern : String
    , togglePatternHelpText : String
    , cardSubtitlePattern : String
    , searchMenu : String
    , searchMenuTagCount : String
    , searchMenuTagCountInfo : String
    , searchMenuCatCount : String
    , searchMenuCatCountInfo : String
    , searchMenuFolderCount : String
    , searchMenuFolderCountInfo : String
    , itemDetail : String
    , browserNativePdfView : String
    , keyboardShortcutLabel : String
    , tagCategoryColors : String
    , colorLabel : Color -> String
    , chooseTagColorLabel : String
    , tagColorDescription : String
    , fields : String
    , fieldsInfo : String
    , fieldLabel : Field -> String
    , templateHelpMessage : String
    }


gb : Texts
gb =
    { basics = Messages.Basics.gb
    , general = "General"
    , showSideMenuByDefault = "Show side menu by default"
    , uiLanguage = "UI Language"
    , itemSearch = "Item Search"
    , maxResultsPerPageInfo =
        \max ->
            "Maximum results in one page when searching items. At most "
                ++ String.fromInt max
                ++ "."
    , maxResultsPerPage = "Page size"
    , showBasicSearchStatsByDefault = "Show basic search statistics by default"
    , enablePowerSearch = "Enable power-user search bar"
    , itemCards = "Item Cards"
    , maxNoteSize = "Max. Note Length"
    , maxNoteSizeInfo =
        \max ->
            "Maximum size of the item notes to display in card view. Between 0 - "
                ++ String.fromInt max
                ++ "."
    , sizeOfItemPreview = "Size of item preview"
    , cardTitlePattern = "Card Title Pattern"
    , togglePatternHelpText = "Toggle pattern help text"
    , cardSubtitlePattern = "Card Subtitle Pattern"
    , searchMenu = "Search Menu"
    , searchMenuTagCount = "Number of tags in search menu"
    , searchMenuTagCountInfo = "How many tags to display in search menu at once. Others can be expanded. Use 0 to always show all."
    , searchMenuCatCount = "Number of categories in search menu"
    , searchMenuCatCountInfo = "How many categories to display in search menu at once. Others can be expanded. Use 0 to always show all."
    , searchMenuFolderCount = "Number of folders in search menu"
    , searchMenuFolderCountInfo = "How many folders to display in search menu at once. Other folders can be expanded. Use 0 to always show all."
    , itemDetail = "Item Detail"
    , browserNativePdfView = "Browser-native PDF preview"
    , keyboardShortcutLabel = "Use keyboard shortcuts for navigation and confirm/unconfirm with open edit menu."
    , tagCategoryColors = "Tag Category Colors"
    , colorLabel = Messages.Data.Color.gb
    , chooseTagColorLabel = "Choose color for tag categories"
    , tagColorDescription = "Tags can be represented differently based on their category."
    , fields = "Fields"
    , fieldsInfo = "Choose which fields to display in search and edit menus."
    , fieldLabel = Messages.Data.Fields.gb
    , templateHelpMessage =
        """
A pattern allows to customize the title and subtitle of each card.
Variables expressions are enclosed in `{{` and `}}`, other text is
used as-is. The following variables are available:

- `{{name}}` the item name
- `{{source}}` the source the item was created from
- `{{folder}}` the items folder
- `{{corrOrg}}` the correspondent organization
- `{{corrPerson}}` the correspondent person
- `{{correspondent}}` both organization and person separated by a comma
- `{{concPerson}}` the concerning person
- `{{concEquip}}` the concerning equipment
- `{{concerning}}` both person and equipment separated by a comma
- `{{fileCount}}` the number of attachments of this item
- `{{dateLong}}` the item date as full formatted date
- `{{dateShort}}` the item date as short formatted date (yyyy/mm/dd)
- `{{dueDateLong}}` the item due date as full formatted date
- `{{dueDateShort}}` the item due date as short formatted date (yyyy/mm/dd)
- `{{direction}}` the items direction values as string

If some variable is not present, an empty string is rendered. You can
combine multiple variables with `|` to use the first non-empty one,
for example `{{corrOrg|corrPerson|-}}` would render the organization
and if that is not present the person. If both are absent a dash `-`
is rendered.
"""
    }
