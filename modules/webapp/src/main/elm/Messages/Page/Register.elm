{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Messages.Page.Register exposing
    ( Texts
    , de
    , gb
    )

import Http
import Messages.Basics
import Messages.Comp.HttpError


type alias Texts =
    { basics : Messages.Basics.Texts
    , httpError : Http.Error -> String
    , signupToDocspell : String
    , collectiveId : String
    , collective : String
    , userLogin : String
    , username : String
    , password : String
    , passwordRepeat : String
    , invitationKey : String
    , alreadySignedUp : String
    , signIn : String
    , registrationSuccessful : String
    , passwordsDontMatch : String
    , allFieldsRequired : String
    }


gb : Texts
gb =
    { basics = Messages.Basics.gb
    , httpError = Messages.Comp.HttpError.gb
    , signupToDocspell = "Signup to Docspell"
    , collectiveId = "Collective ID"
    , collective = "Collective"
    , userLogin = "User Login"
    , username = "Username"
    , password = "Password"
    , passwordRepeat = "Password (repeat)"
    , invitationKey = "Invitation Key"
    , alreadySignedUp = "Already signed up?"
    , signIn = "Sign in"
    , registrationSuccessful = "Registration successful."
    , passwordsDontMatch = "The passwords do not match."
    , allFieldsRequired = "All fields are required!"
    }


de : Texts
de =
    { basics = Messages.Basics.de
    , httpError = Messages.Comp.HttpError.de
    , signupToDocspell = "Registrierung bei Docspell"
    , collectiveId = "Kollektiv"
    , collective = "Kollektiv"
    , userLogin = "Benutzername"
    , username = "Benutzername"
    , password = "Passwort"
    , passwordRepeat = "Passwort (Wiederholung)"
    , invitationKey = "Einladungs-ID"
    , alreadySignedUp = "Bereits registriert?"
    , signIn = "Anmelden"
    , registrationSuccessful = "Registratierung erfolgreich."
    , passwordsDontMatch = "Die Passwörten stimmen nicht überein."
    , allFieldsRequired = "Alle Felder müssen ausgefüllt werden!"
    }
