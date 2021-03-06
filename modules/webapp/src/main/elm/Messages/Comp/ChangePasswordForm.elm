module Messages.Comp.ChangePasswordForm exposing (Texts, gb)

import Messages.Basics


type alias Texts =
    { basics : Messages.Basics.Texts
    , currentPassword : String
    , newPassword : String
    , repeatPassword : String
    , currentPasswordPlaceholder : String
    , newPasswordPlaceholder : String
    , repeatPasswordPlaceholder : String
    }


gb : Texts
gb =
    { basics = Messages.Basics.gb
    , currentPassword = "Current Password"
    , newPassword = "New Password"
    , repeatPassword = "New Password (repeat)"
    , currentPasswordPlaceholder = "Password"
    , newPasswordPlaceholder = "Password"
    , repeatPasswordPlaceholder = "Password"
    }
