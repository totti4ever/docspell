{-
  Copyright 2020 Docspell Contributors

  SPDX-License-Identifier: GPL-3.0-or-later
-}

module Comp.DatePicker exposing
    ( Msg
    , defaultSettings
    , emptyModel
    , endOfDay
    , init
    , midOfDay
    , startOfDay
    , update
    , updateDefault
    , view
    , viewTime
    , viewTimeDefault
    )

import Date exposing (Date)
import DatePicker exposing (DateEvent, DatePicker, Settings)
import Html exposing (Html)
import Time exposing (Month(..), utc)


type alias Msg =
    DatePicker.Msg


init : ( DatePicker, Cmd Msg )
init =
    DatePicker.init


emptyModel : DatePicker
emptyModel =
    DatePicker.initFromDate (Date.fromCalendarDate 2019 Aug 21)


defaultSettings : Settings
defaultSettings =
    let
        ds =
            DatePicker.defaultSettings
    in
    { ds
        | changeYear = DatePicker.from 2010
        , parser =
            \str ->
                ds.parser str
                    |> Result.mapError (\_ -> str)
    }


update : Settings -> Msg -> DatePicker -> ( DatePicker, DateEvent )
update settings msg model =
    DatePicker.update settings msg model


updateDefault : Msg -> DatePicker -> ( DatePicker, DateEvent )
updateDefault msg model =
    DatePicker.update defaultSettings msg model


view : Maybe Date -> Settings -> DatePicker -> Html Msg
view md settings model =
    DatePicker.view md settings model


viewTime : Maybe Int -> Settings -> DatePicker -> Html Msg
viewTime md settings model =
    let
        date =
            Maybe.map Time.millisToPosix md
                |> Maybe.map (Date.fromPosix Time.utc)
    in
    view date settings model


viewTimeDefault : Maybe Int -> DatePicker -> Html Msg
viewTimeDefault md model =
    viewTime md defaultSettings model


startOfDay : Date -> Int
startOfDay date =
    let
        unix0 =
            Date.fromPosix Time.utc (Time.millisToPosix 0)

        days =
            Date.diff Date.Days unix0 date
    in
    days * 24 * 60 * 60 * 1000


endOfDay : Date -> Int
endOfDay date =
    startOfDay date + ((24 * 60) - 1) * 60 * 1000


midOfDay : Date -> Int
midOfDay date =
    startOfDay date + (12 * 60 * 60 * 1000)
