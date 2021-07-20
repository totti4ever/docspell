{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Page.Login.Update exposing (update)

import Api
import Api.Model.AuthResult exposing (AuthResult)
import Data.Flags exposing (Flags)
import Page exposing (Page(..))
import Page.Login.Data exposing (..)
import Ports


update : Maybe Page -> Flags -> Msg -> Model -> ( Model, Cmd Msg, Maybe AuthResult )
update referrer flags msg model =
    case msg of
        SetUsername str ->
            ( { model | username = str }, Cmd.none, Nothing )

        SetPassword str ->
            ( { model | password = str }, Cmd.none, Nothing )

        ToggleRememberMe ->
            ( { model | rememberMe = not model.rememberMe }, Cmd.none, Nothing )

        Authenticate ->
            let
                userPass =
                    { account = model.username
                    , password = model.password
                    , rememberMe = Just model.rememberMe
                    }
            in
            ( model, Api.login flags userPass AuthResp, Nothing )

        AuthResp (Ok lr) ->
            let
                gotoRef =
                    Maybe.withDefault HomePage referrer |> Page.goto
            in
            if lr.success then
                ( { model | formState = AuthSuccess lr, password = "" }
                , Cmd.batch [ setAccount lr, gotoRef ]
                , Just lr
                )

            else
                ( { model | formState = AuthFailed lr, password = "" }
                , Ports.removeAccount ()
                , Just lr
                )

        AuthResp (Err err) ->
            let
                empty =
                    Api.Model.AuthResult.empty
            in
            ( { model | password = "", formState = HttpError err }
            , Ports.removeAccount ()
            , Just empty
            )


setAccount : AuthResult -> Cmd msg
setAccount result =
    if result.success then
        Ports.setAccount result

    else
        Ports.removeAccount ()
