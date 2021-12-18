#include <iostream>
#include "app.h"

#include "user_interface.h"
#include "backend.h"
#include "frontend.h"


App::App(std::istream& is, std::ostream& os): is(is), os(os) {
    // TODO
}

void App::run() {
    // TODO
    UserInterface ui;
    BackEnd backend;
    FrontEnd frontend(ui, backend);
    ui.create_ui(&frontend, is, os);
    ui.run();
}

